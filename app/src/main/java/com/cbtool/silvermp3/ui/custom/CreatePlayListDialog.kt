package com.cbtool.silvermp3.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cbtool.silvermp3.data.repository.firestore.UserPlaylistRepository
import com.cbtool.silvermp3.databinding.LayoutCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject


class CreatePlayListDialog : DialogFragment() {

    private var _binding: LayoutCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistRepo: UserPlaylistRepository by inject()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LayoutCreatePlaylistBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
        dialog.setOnShowListener {
            binding.closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            binding.createBtn.setOnClickListener {
                val name = binding.edtPlaylistName.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập tên danh sách phát", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    playlistRepo.add(name)
                    dialog.dismiss()
                }
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

