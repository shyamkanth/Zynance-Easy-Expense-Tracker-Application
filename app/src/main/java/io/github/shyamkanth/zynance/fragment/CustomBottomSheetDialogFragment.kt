package io.github.shyamkanth.zynance.fragment
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.shyamkanth.zynance.R
import io.github.shyamkanth.zynance.databinding.BottomSheetLayoutBinding
import io.github.shyamkanth.zynance.utils.Utils

class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun showDialog(activity: FragmentActivity, type: Utils.BsType, header: String, content: String) {
            val bottomSheetFragment = CustomBottomSheetDialogFragment()
            val args = Bundle()
            args.putSerializable("TYPE", type)
            args.putString("HEADER", header)
            args.putString("CONTENT", content)
            bottomSheetFragment.arguments = args
            bottomSheetFragment.show(activity.supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        val type = arguments?.getSerializable("TYPE", Utils.BsType::class.java)
        val header = arguments?.getString("HEADER")
        val content = arguments?.getString("CONTENT")

        when(type){
            Utils.BsType.INFO -> {
                binding.bottomSheetType.setTextColor(requireActivity().getColor(R.color.primary_info))
                binding.bottomSheetHeader.setTextColor(requireActivity().getColor(R.color.primary_info))
                binding.closeButtonBtm.setBackgroundColor(requireActivity().getColor(R.color.primary_info))
            }
            Utils.BsType.SUCCESS -> {
                binding.bottomSheetType.setTextColor(requireActivity().getColor(R.color.primary))
                binding.bottomSheetHeader.setTextColor(requireActivity().getColor(R.color.primary))
                binding.closeButtonBtm.setBackgroundColor(requireActivity().getColor(R.color.primary))
            }
            Utils.BsType.ERROR -> {
                binding.bottomSheetType.setTextColor(requireActivity().getColor(R.color.primary_error))
                binding.bottomSheetHeader.setTextColor(requireActivity().getColor(R.color.primary_error))
                binding.closeButtonBtm.setBackgroundColor(requireActivity().getColor(R.color.primary_error))
            }
            null -> {}
        }
        binding.bottomSheetType.text = type.toString()
        binding.bottomSheetHeader.text = header
        binding.bottomSheetContent.text = content
        binding.closeButtonBtm.text = "CLOSE"
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.closeButtonBtm.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
