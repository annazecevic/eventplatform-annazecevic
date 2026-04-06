import { Component, OnInit } from '@angular/core';
import { EventService, Event } from './event.service';
import { Router } from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-homepage',
  imports: [CommonModule, FormsModule],
  templateUrl: './homepage.html',
  styleUrl: './homepage.scss'
})
export class Homepage implements OnInit {
  eventsToday: Event[] = [];
  filteredEvents: Event[] = [];
  isLoggedIn: boolean = false;
  username: string = '';

  // Filter/search fields
  filterType: string = '';
  filterAddress: string = '';
  filterPrice: number | null = null;
  filterName: string = '';

  constructor(private eventService: EventService, private router: Router) {}

  ngOnInit() {
    this.eventService.getEventsToday().subscribe(events => {
      this.eventsToday = events;
      this.filteredEvents = events;
      console.log('Danasnji eventi:', events);
    });
    // Prosta provera statusa (može se proširiti na JWT/token)
    const token = localStorage.getItem('token') ?? '';
    const payload = JSON.parse(atob(token.split('.')[1]));
    if (payload) {
      this.isLoggedIn = true;
      this.username = payload.sub;
    }
  }

  filterEvents() {
    this.eventService.searchEvents({
      name: this.filterName,
      type: this.filterType,
      address: this.filterAddress,
      maxPrice: this.filterPrice !== null ? this.filterPrice : undefined
    }).subscribe(events => {
      this.filteredEvents = events;
    });
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

  goToLocations() {
    this.router.navigate(['/locations']);
  }

  logout() {
    localStorage.removeItem('user');
    this.isLoggedIn = false;
    this.username = '';
    this.router.navigate(['/']);
  }
}
