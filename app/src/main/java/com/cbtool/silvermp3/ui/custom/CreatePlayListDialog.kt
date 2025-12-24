package com.cbtool.silvermp3.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cbtool.silvermp3.databinding.LayoutCreatePlaylistBinding
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.library.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class CreatePlayListDialog : DialogFragment() {

    private var _binding: LayoutCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by activityViewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val count = arguments?.getInt(ARG_COUNT) ?: 0
        _binding = LayoutCreatePlaylistBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
        dialog.setOnShowListener {
            binding.edtPlaylistName.setText("Danh sách phát thứ ${count} của tôi")
            binding.closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            binding.createBtn.setOnClickListener {
                val name = binding.edtPlaylistName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập tên danh sách phát", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    playlistViewModel.addPlaylist(name)
                    dialog.dismiss()
                    libraryViewModel.getPlaylists()
                }
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CreatePlayListDialog"
        const val ARG_COUNT = "count"
        fun newInstance(count: Int) = CreatePlayListDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNT, count)
            }
        }


    }
}

