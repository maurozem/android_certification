package br.com.programadordeelite.gdc.codelab.core.toast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.programadordeelite.gdc.R
import br.com.programadordeelite.gdc.codelab.util.snake
import br.com.programadordeelite.gdc.codelab.util.toast
import br.com.programadordeelite.gdc.databinding.FragmentToastSnakeBinding
import com.google.android.material.snackbar.Snackbar

class ToastSnakeFragment : Fragment(R.layout.fragment_toast_snake) {

    private lateinit var binding: FragmentToastSnakeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentToastSnakeBinding.bind(view)
        setListners()
    }

    private fun setListners() {
        binding.toast.setOnClickListener {
            toast("elemetar meu caro")
        }
        binding.snake.setOnClickListener {
            snake(binding.root, "snake é cobra")
        }
        binding.snakeAction.setOnClickListener {
            Snackbar
                .make(binding.root, "atenção: snake é cobra!!", Snackbar.LENGTH_LONG)
                .setAction("okay") {
                    toast("parabéns")
                }
                .show()
        }
    }
}
