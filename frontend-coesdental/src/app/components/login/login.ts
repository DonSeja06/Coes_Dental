import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService) {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    const token = localStorage.getItem('token');
    const rol = localStorage.getItem('rol');

    if (token) {
      if (rol === 'ROLE_Odontologo') {
        this.router.navigate(['/layout/mis-citas']);
      } else {
        this.router.navigate(['/layout/dashboard']);
      }
    }
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (respuesta) => {
          console.log('¡Login súper exitoso!', respuesta);
          alert('¡Bienvenido al sistema!');
          this.router.navigate(['/layout']);
        },
        error: (error) => {
          console.error('Error en el login', error);
          alert('Correo o contraseña incorrectos. Intenta de nuevo.');
        }
      });
    }
  }
}