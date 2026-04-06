import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Potrebno za *ngIf
import { FormsModule } from '@angular/forms'; // Potrebno za [(ngModel)]
import { HttpClient, HttpHeaders } from '@angular/common/http'; // Dodaj HttpHeaders
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule], // Dodaj FormsModule
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss']
})
export class Profile implements OnInit {
  // Profilni podaci
  name: string = '';
  userEmail: string = ''; // Pretpostavka da ćeš dohvatiti email
  phoneNumber: string = '';
  address: string = '';
  city: string = '';
  editMode: boolean = false;
  profileError: string = '';

  // Podaci za promenu lozinke
  showChangePasswordModal: boolean = false;
  currentPassword = '';
  newPassword = '';
  repeatNewPassword = '';
  passwordError = '';

  // Privremeno za čuvanje originalnih podataka pre edita (ako otkaže)
  private originalProfileData: any;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfileData();
  }

  loadProfileData() {
    // Uzmi token iz localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      this.profileError = 'User not authenticated.';
      this.router.navigate(['/login']); // Redirektuj na login ako nema tokena
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    // PRIMER: Dohvati profilne podatke sa backend-a
    this.http.get<any>('http://localhost:8080/api/users/profile', { headers }).subscribe({
      next: (data) => {
        this.name = data.name || localStorage.getItem('userName') || ''; // Ako backend ne vraća name, uzmi iz localStorage
        this.userEmail = data.email || '';
        this.phoneNumber = data.phoneNumber || '';
        this.address = data.address || '';
        this.city = data.city || '';
        this.originalProfileData = { ...data }; // Sačuvaj originalne podatke
      },
      error: (err) => {
        console.error('Failed to load profile data', err);
        this.profileError = 'Failed to load profile data. Please try again later.';
        if (err.status === 401) { // Unauthorized
          this.router.navigate(['/login']);
        }
      }
    });
  }

  enableEdit() {
    this.originalProfileData = { // Sačuvaj trenutne podatke pre ulaska u edit mode
      name: this.name,
      phoneNumber: this.phoneNumber,
      address: this.address,
      city: this.city,
      email: this.userEmail // Ako se email ne menja, zadrži ga
    };
    this.editMode = true;
  }

  cancelEdit() {
    // Vrati se na originalne podatke
    if (this.originalProfileData) {
      this.name = this.originalProfileData.name;
      this.phoneNumber = this.originalProfileData.phoneNumber;
      this.address = this.originalProfileData.address;
      this.city = this.originalProfileData.city;
      this.userEmail = this.originalProfileData.email;
    }
    this.editMode = false;
    this.profileError = ''; // Obriši eventualne greške
  }

  saveProfile() {
    // Uzmi token iz localStorage
    const token = localStorage.getItem('token');
    if (!token) {
      this.profileError = 'User not authenticated.';
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const updatedProfile = {
      name: this.name,
      phoneNumber: this.phoneNumber,
      address: this.address,
      city: this.city,
      // Možda i email, ali često se email menja kroz poseban proces
      // email: this.userEmail
    };

    this.http.put<any>('http://localhost:8080/api/users/profile', updatedProfile, { headers }).subscribe({
      next: (res) => {
        alert('Profile updated successfully!');
        // Ažuriraj lokalni userName ako je ime promenjeno
        if (res.name) {
          localStorage.setItem('userName', res.name);
        }
        this.editMode = false;
        this.profileError = '';
        this.loadProfileData(); // Ponovo učitaj podatke da budu sinhronizovani
      },
      error: (err) => {
        console.error('Failed to update profile', err);
        this.profileError = 'Failed to update profile. Please try again.';
      }
    });
  }

  openChangePasswordModal() {
    this.showChangePasswordModal = true;
    this.passwordError = ''; // Resetuj grešku pri otvaranju modala
    this.currentPassword = ''; // Resetuj polja
    this.newPassword = '';
    this.repeatNewPassword = '';
  }

  closeChangePasswordModal() {
    this.showChangePasswordModal = false;
    this.passwordError = '';
    this.currentPassword = '';
    this.newPassword = '';
    this.repeatNewPassword = '';
  }

  async changePassword() {
    this.passwordError = ''; // Resetuj prethodne greške

    // 1. Validacija: provera da li su sva polja popunjena
    if (!this.currentPassword || !this.newPassword || !this.repeatNewPassword) {
      this.passwordError = 'All password fields are required.';
      return;
    }

    // 2. Validacija: Nova lozinka i ponovljena nova lozinka se moraju podudarati
    if (this.newPassword !== this.repeatNewPassword) {
      this.passwordError = 'New password and repeat new password do not match.';
      return;
    }

    // 3. Validacija: Nova lozinka ne sme biti ista kao stara
    if (this.newPassword === this.currentPassword) {
      this.passwordError = 'New password cannot be the same as the current password.';
      return;
    }

    // Dodatna validacija za jačinu lozinke (opciono, ali preporučljivo)
    if (this.newPassword.length < 8) {
      this.passwordError = 'New password must be at least 8 characters long.';
      return;
    }
    // Možeš dodati i regex za simbole, brojeve, velika slova itd.

    // Sve validacije su prošle na klijentskoj strani, sada šaljemo na backend
    const token = localStorage.getItem('token');
    if (!token) {
      this.passwordError = 'User not authenticated.';
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const passwordChangeData = {
      currentPassword: this.currentPassword,
      newPassword: this.newPassword
    };

    try {
      // PRIMER: Endpoint za promenu lozinke
      await this.http.put<any>('http://localhost:8080/api/auth/change-password', passwordChangeData, { headers }).toPromise();
      alert('Password changed successfully!');
      this.closeChangePasswordModal(); // Zatvori modal
    } catch (err: any) {
      console.error('Failed to change password', err);
      if (err.status === 400 && err.error && err.error.message) {
        // Pretpostavljamo da backend vraća specifične poruke za greške
        this.passwordError = err.error.message;
      } else if (err.status === 401) {
        this.passwordError = 'Unauthorized. Please login again.';
        this.router.navigate(['/login']);
      }
      else {
        this.passwordError = 'Failed to change password. Please check your current password and try again.';
      }
    }
  }


  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('userName');
    this.router.navigate(['/']);
  }
}
