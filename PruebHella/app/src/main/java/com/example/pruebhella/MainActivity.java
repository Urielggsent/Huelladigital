package com.example.pruebhella;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_BIOMETRIC = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Comprobar si el dispositivo tiene el hardware y es compatible con la autenticación biométrica
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED) {
                // El dispositivo es compatible con la autenticación biométrica, puedes continuar con la lógica de tu aplicación
                showBiometricPrompt();
            } else {
                // Solicitar permiso para usar la autenticación biométrica
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_BIOMETRIC}, REQUEST_CODE_BIOMETRIC);
            }
        } else {
            // La versión de Android es anterior a la 6.0, lo que no admite la autenticación biométrica
            Toast.makeText(this, "La autenticación biométrica no es compatible con este dispositivo.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_BIOMETRIC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes continuar con la lógica de tu aplicación
                showBiometricPrompt();
            } else {
                // Permiso denegado, debes manejarlo según el flujo de tu aplicación
            }
        }
    }

    // Método para mostrar el diálogo de autenticación biométrica
    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // Se llama cuando ocurre un error durante la autenticación
                Toast.makeText(MainActivity.this, "Error en la autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                // Se llama cuando la autenticación es exitosa
                Toast.makeText(MainActivity.this, "¡Autenticación exitosa!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                // Se llama cuando la autenticación falla
                Toast.makeText(MainActivity.this, "Autenticación fallida.", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Título del diálogo de autenticación")
                .setSubtitle("Subtítulo del diálogo de autenticación")
                .setDescription("Descripción del diálogo de autenticación")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
