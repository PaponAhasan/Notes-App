package com.example.notesappadvance.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesappadvance.MainActivity
import com.example.notesappadvance.R
import com.example.notesappadvance.databinding.FragmentLoginBinding
import com.example.notesappadvance.models.login.LoginRequest
import com.example.notesappadvance.models.registation.RegistationRequest
import com.example.notesappadvance.utils.Helper
import com.example.notesappadvance.utils.NetworkResult
import com.example.notesappadvance.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private val authViewModel by activityViewModels<AuthViewModel>()

    private var observeJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBinding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }

        loginBinding.btnLogin.setOnClickListener {
            Helper.hideKeyboard(view)
            loginBinding.progressBar.visibility = View.VISIBLE

            val validationResult = validateUserInput()
            if (!validationResult.first) {
                showValidationErrors(validationResult.second)
                return@setOnClickListener
            }

            loginBinding.txtError.text = ""
            val registrationRequest = getUserRequest()

            authViewModel.requestLogin(
                registrationRequest
            )

        }

        bindObserve()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = loginBinding.txtEmail.text.toString()
        val password = loginBinding.txtPassword.text.toString()
        return authViewModel.validateCredentials(emailAddress, "", password, true)
    }

    private fun showValidationErrors(error: String) {
        loginBinding.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun getUserRequest(): LoginRequest {
        return loginBinding.run {
            LoginRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
            )
        }
    }

    private fun bindObserve() {
        observeJob = lifecycleScope.launch {
            authViewModel.loginState.collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        loginBinding.progressBar.visibility = View.VISIBLE
                    }

                    is NetworkResult.Empty -> {
                        loginBinding.progressBar.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        loginBinding.progressBar.visibility = View.GONE
                        loginBinding.txtError.text = it.message
                    }

                    is NetworkResult.Success -> {
                        loginBinding.progressBar.visibility = View.GONE
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent).also { requireActivity().finish() }
                    }
                }
            }
        }
    }
}