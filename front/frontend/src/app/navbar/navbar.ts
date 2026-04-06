import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router'; // Dodaj NavigationEnd
import { filter } from 'rxjs/operators'; // Dodaj filter

@Component({
  selector: 'app-navbar',
  imports: [CommonModule],
  templateUrl: './navbar.html',
  standalone: true,
  styleUrl: './navbar.scss'
})
export class Navbar implements OnInit {
  isLoggedIn: boolean = false;
  username: string = '';

  constructor(private router: Router) {}

  ngOnInit() {
    this.checkLoginStatus(); // Proveri status pri učitavanju komponente

    // Osluškuj promene rute da bi ažurirao navbar kada se korisnik prijavi/odjavi
    // Ovo je korisno ako se login/logout dešava bez potpunog osvežavanja stranice
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.checkLoginStatus(); // Ponovo proveri status pri navigaciji
    });
  }

  checkLoginStatus() {
    const token = localStorage.getItem('token'); // Proveravamo da li postoji token
    const userName = localStorage.getItem('userName'); // Dohvatamo username

    if (token && userName) { // Ako postoje i token i username, korisnik je ulogovan
      this.isLoggedIn = true;
      this.username = userName;
    } else {
      this.isLoggedIn = false;
      this.username = '';
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }

  goToProfile() {
    this.router.navigate(['/profile']);
  }
  goToHomePage() {
    this.router.navigate(['/homepage']);
  }

  goToLocations() {
    this.router.navigate(['/locations']);
  }

  logout() {
    localStorage.removeItem('token');     // Ukloni token
    localStorage.removeItem('isAdmin');   // Ukloni isAdmin
    localStorage.removeItem('userName');  // Ukloni userName
    this.isLoggedIn = false;
    this.username = '';
    this.router.navigate(['/']); // Redirektuj na početnu stranu nakon odjave
  }
}
