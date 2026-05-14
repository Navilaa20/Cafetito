import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Rol } from '../../../core/models/login-response.dto';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  form: FormGroup;
  errorMessage = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
  ) {
    this.form = this.fb.nonNullable.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  get username() {
    return this.form.get('username');
  }
  get password() {
    return this.form.get('password');
  }

  get canSubmit(): boolean {
    return this.form.valid && !this.loading;
  }

  readonly MSG_ERROR_AUTH = 'Error al autenticar. Verifique usuario y contrasena.';

  onSubmit(): void {
    this.errorMessage = '';
    if (!this.form.valid) return;
    this.loading = true;
    this.auth.login(this.form.getRawValue()).subscribe({
      next: (res) => {
        this.loading = false;
        this.redirectByRol(res.rol);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = this.MSG_ERROR_AUTH;
      },
    });
  }

  private redirectByRol(rol: Rol): void {
    switch (rol) {
      case 'ROLE_AGRICULTOR':
        this.router.navigate(['/dashboard/agricultor']);
        break;
      case 'ROLE_BENEFICIO':
        this.router.navigate(['/dashboard/administrador']);
        break;
      case 'ROLE_PESOCABAL':
        this.router.navigate(['/dashboard/pesaje']);
        break;
      default:
        this.router.navigate(['/login']);
    }
  }
}
