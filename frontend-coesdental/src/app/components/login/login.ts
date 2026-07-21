import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth';
import Swal from 'sweetalert2';

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
      } else if (rol === 'ROLE_Paciente') {
        this.router.navigate(['/layout/mis-citas-paciente']);
      } else if (rol === 'ROLE_Recepcionista') {
        this.router.navigate(['/layout/calendario']);
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
          
          Swal.fire({
            icon: 'success',
            title: '¡Bienvenido(a)!',
            text: 'Sesión iniciada correctamente.',
            showConfirmButton: false,
            timer: 1500,
            backdrop: `rgba(0,0,123,0.4)`
          }).then(() => {
            const rolGuardado = localStorage.getItem('rol');
            if (rolGuardado === 'ROLE_Odontologo') {
              this.router.navigate(['/layout/mis-citas']);
            } else if (rolGuardado === 'ROLE_Paciente') {
              this.router.navigate(['/layout/mis-citas-paciente']);
            } else if (rolGuardado === 'ROLE_Recepcionista') {
              this.router.navigate(['/layout/calendario']);
            } else {
              this.router.navigate(['/layout/dashboard']);
            }
          });
        },
        error: (error) => {
          console.error('Error en el login', error);
          Swal.fire({
            icon: 'error',
            title: 'Acceso Denegado',
            text: 'Correo o contraseña incorrectos. Intenta de nuevo.',
            confirmButtonColor: '#0a4275'
          });
        }
      });
    }
  }
}