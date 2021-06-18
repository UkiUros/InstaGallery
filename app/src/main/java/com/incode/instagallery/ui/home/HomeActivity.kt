package com.incode.instagallery.ui.home

import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.incode.instagallery.BuildConfig
import com.incode.instagallery.databinding.ActivityHomeBinding
import com.incode.instagallery.domain.DataState
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.ui.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = FeedAdapter()

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Timber.d("Photo captured")
                addPhotoToDbAndGallery()

            }
        }

    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.isEnabled = false // just using it as indicator

        binding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object : FeedAdapter.OnItemClickListener {
            override fun onItemClicked(feed: Feed) {
                DetailsActivity.start(this@HomeActivity, feed.id)
            }
        }

        binding.buttonCamera.setOnClickListener {
            openCamera()
        }

        subscribeToObservables()
        viewModel.loadFeeds(true)

    }

    private fun subscribeToObservables() {
        viewModel.feedDataState.observe(this, {
            when (it) {
                is DataState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Timber.e(it.throwable)
                }
                DataState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is DataState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    adapter.submitList(it.data)
                }
            }
        })

        viewModel.saveFeedPhotoDataState.observe(this, {
            when (it) {
                is DataState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Timber.e(it.throwable)
                }
                DataState.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                is DataState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    viewModel.loadFeeds() // loading from db only
                }
            }
        })
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this,
                            TextUtils.concat(BuildConfig.APPLICATION_ID, ".fileprovider").toString(),
                            it
                        )
                    } else {
                        Uri.fromFile(it);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLauncher.launch(takePictureIntent)
                }
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun addPhotoToDbAndGallery() {
        currentPhotoPath?.let {
            val f = File(it)
            MediaScannerConnection.scanFile(
                this, arrayOf(f.toString()),
                null, null
            )

            viewModel.addLocalPhoto(it)
        }

    }

}
