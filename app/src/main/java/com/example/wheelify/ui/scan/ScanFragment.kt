package com.example.wheelify.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wheelify.databinding.FragmentScanBinding
import com.example.wheelify.ui.preview.PreviewActivity
import com.example.wheelify.ui.scan.CameraActivity.Companion.CAMERAX_RESULT
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Suppress("DEPRECATION")
class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private lateinit var scanViewModel: ScanViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scanViewModel = ViewModelProvider(this)[ScanViewModel::class.java]

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentImageUri = savedInstanceState?.getParcelable(KEY_IMAGE_URI)
        currentImageUri?.let {
            showImage()
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraxButton.setOnClickListener { startCameraX() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let {
            outState.putParcelable(KEY_IMAGE_URI, it)
            scanViewModel.currentImageUri = it
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            Toast.makeText(requireContext(), "Tidak ada media yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private val uCropActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    currentImageUri = resultUri
                    showImage(resultUri)
                } else {
                    showToast("Error: Gagal mendapatkan URI gambar yang dipotong")
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(result.data!!)
                showToast("Error: ${error?.message}")
            }
        }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image.jpg"))
        val cropIntent = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .getIntent(requireContext())
        uCropActivityResult.launch(cropIntent)
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            startCrop(uri = currentImageUri!!)
            scanViewModel.currentImageUri
            showImage()
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                scanViewModel.uploadImage(multipartBody)
                val intent = Intent(requireContext(), PreviewActivity::class.java)
                intent.putExtra(PreviewActivity.EXTRA_IMG_URI, uri.toString())
                intent.putExtra(PreviewActivity.EXTRA_RESULT, scanViewModel.scanResult.value?.jsonMemberClass)
                startActivity(intent)
            }



        } ?: showToast("Error: Failed to upload image")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage(uri: Uri? = currentImageUri) {
        uri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val KEY_IMAGE_URI = "key_image_uri"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

    }
}