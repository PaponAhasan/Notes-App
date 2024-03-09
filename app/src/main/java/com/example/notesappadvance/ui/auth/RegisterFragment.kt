package com.example.notesappadvance.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesappadvance.MainActivity
import com.example.notesappadvance.R
import com.example.notesappadvance.databinding.FragmentRegisterBinding
import com.example.notesappadvance.models.registation.RegistationRequest
import com.example.notesappadvance.repository.UserRepository
import com.example.notesappadvance.utils.Helper
import com.example.notesappadvance.utils.NetworkResult
import com.example.notesappadvance.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var registerBinding: FragmentRegisterBinding
    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var userRepository: UserRepository

    private var observeJob: Job? = null

    companion object {
        const val url = "https://picsum.photos/800"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        registerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        lifecycleScope.launch {
            val token = userRepository.dataStore.getToken()
                .firstOrNull() // Collect only the first value or null
            if (!token.isNullOrEmpty()) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Finish the fragment after starting the activity
            } else {
                // Handle the case where there's no token or an empty token (optional)
                Timber.d("No valid token found")
            }
        }

        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBinding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        registerBinding.btnSignUp.setOnClickListener {

            Helper.hideKeyboard(view)
            registerBinding.progressBar.visibility = View.VISIBLE

            val validationResult = validateUserInput()
            if (!validationResult.first) {
                showValidationErrors(validationResult.second)
                return@setOnClickListener
            }

            registerBinding.txtError.text = ""
            val registrationRequest = getUserRequest()
            authViewModel.requestSignup(
                registrationRequest
            )
        }
        bindObserve()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = registerBinding.txtEmail.text.toString()
        val userName = registerBinding.txtUsername.text.toString()
        val password = registerBinding.txtPassword.text.toString()
        return authViewModel.validateCredentials(emailAddress, userName, password, false)
    }

    private fun showValidationErrors(error: String) {
        registerBinding.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun getUserRequest(): RegistationRequest {
        return registerBinding.run {
            RegistationRequest(
                txtUsername.text.toString(),
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                url
            )
        }
    }

    private fun bindObserve() {
        observeJob = lifecycleScope.launch {
            authViewModel.signUpState.collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        registerBinding.progressBar.visibility = View.VISIBLE
                    }

                    is NetworkResult.Empty -> {
                        registerBinding.progressBar.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        registerBinding.progressBar.visibility = View.GONE
                        registerBinding.txtError.text = it.message
                    }

                    is NetworkResult.Success -> {
                        registerBinding.progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}