import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',   // ✅ corrected
  styleUrls: ['./profile.scss']    // also make sure this matches
})
export class ProfileComponent {
  userEmail: string | null = null;

  showChangePasswordModal = false;
  currentPassword = '';
  newPassword = '';
  repeatNewPassword = '';
  passwordError = '';

  name: string = '';
  phoneNumber: string = '';
  address: string = '';
  city: string = '';
  editMode: boolean = false;
  profileError: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.userEmail = payload.sub;
      this.loadProfile();
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }

  openChangePasswordModal() {
    this.showChangePasswordModal = true;
    this.currentPassword = '';
    this.newPassword = '';
    this.repeatNewPassword = '';
    this.passwordError = '';
  }

  closeChangePasswordModal() {
    this.showChangePasswordModal = false;
    this.passwordError = '';
  }

  changePassword() {
    if (this.newPassword !== this.repeatNewPassword) {
      this.passwordError = 'Nove lozinke se ne poklapaju.';
      return;
    }
    if (!this.currentPassword || !this.newPassword) {
      this.passwordError = 'Popunite sva polja.';
      return;
    }
    if (this.newPassword === this.currentPassword) {
      this.passwordError = 'Nova lozinka ne može biti ista kao trenutna lozinka.';
      return;
    }
    this.http.post('http://localhost:8080/api/users/change-password', {
      email: this.userEmail,
      currentPassword: this.currentPassword,
      newPassword: this.newPassword
    }).subscribe({
      next: () => {
        this.closeChangePasswordModal();
        alert('Password changed successfully!');
      },
      error: err => {
        this.passwordError = err.error?.message || 'Error changing password.';
      }
    });
  }

  loadProfile() {
    if (!this.userEmail) return;
    this.http.get<any>(`http://localhost:8080/api/users/${this.userEmail}`).subscribe({
      next: user => {
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.address = user.address;
        this.city = user.city;
      },
      error: () => {
        this.profileError = 'Failed to load profile.';
      }
    });
  }

  enableEdit() {
    this.editMode = true;
    this.profileError = '';
  }

  cancelEdit() {
    this.editMode = false;
    this.loadProfile();
    this.profileError = '';
  }

  saveProfile() {
    if (!this.name || !this.phoneNumber || !this.address || !this.city) {
      this.profileError = 'All fields are required.';
      return;
    }
    this.http.put(`http://localhost:8080/api/users/update`, {
      email: this.userEmail,
      name: this.name,
      phoneNumber: this.phoneNumber,
      address: this.address,
      city: this.city
    }).subscribe({
      next: () => {
        this.editMode = false;
        this.profileError = '';
      },
      error: err => {
        this.profileError = err.error?.message || 'Error updating profile.';
      }
    });
  }
}
