import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
  imports: [ReactiveFormsModule]
})
export class Login {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,

  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) return;

    this.http.post<{ token: string, role: string, name: string }>(
      'http://localhost:8080/api/auth/login',
      this.loginForm.value
    ).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('isAdmin', res.role === 'ADMIN' ? 'true' : 'false');
        localStorage.setItem('userName', res.name);

        alert(`Welcome, ${res.name}!`);

        // 🔹 Redirekcija po ulozi
        if (res.role === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/homepage']);
        }
      },
      error: (err) => {
        console.error('Login failed', err);
        alert('Invalid email or password');
      }
    });
  }




}
