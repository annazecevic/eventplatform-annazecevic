import { Component, OnInit } from '@angular/core';
import { Location } from './location.model';
import { LocationsService } from './locations.service';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-locations',
  imports: [CommonModule, FormsModule],
  templateUrl: './locations.html',
  styleUrl: './locations.scss'
})
export class Locations implements OnInit {
  locations: Location[] = [];
  filterName: string = '';
  filterAddress: string = '';
  filterType: string = '';

  // Review modal
  showReviewModal = false;
  selectedLocation: Location | null = null;
  reviews: any[] = [];

  // Review form
  selectedEventId: number | null = null;
  performanceRating: number | null = null;
  soundLightRating: number | null = null;
  spaceRating: number | null = null;
  overallRating: number | null = null;
  comment: string = '';
  reviewError: string = '';

  events: any[] = [];

  constructor(private locationsService: LocationsService, private http: HttpClient) {}

  ngOnInit() {
    this.locationsService.getAllLocations().subscribe(locations => {
      this.locations = locations;
    });
  }

  filterLocations() {
    this.locationsService.searchLocations({
      name: this.filterName,
      address: this.filterAddress,
      type: this.filterType
    }).subscribe(locations => {
      this.locations = locations;
    });
  }

  openReviewModal(location: Location) {
    this.selectedLocation = location;
    this.showReviewModal = true;
    this.resetReviewForm();
    this.loadReviews(location.id!);
    this.loadEventsForLocation(location.id!);
  }

  closeReviewModal() {
    this.showReviewModal = false;
    this.selectedLocation = null;
    this.resetReviewForm();
    this.events = [];
  }

  resetReviewForm() {
    this.selectedEventId = null;
    this.performanceRating = null;
    this.soundLightRating = null;
    this.spaceRating = null;
    this.overallRating = null;
    this.comment = '';
    this.reviewError = '';
  }

  loadEventsForLocation(locationId: number) {
    this.http.get<any[]>(`http://localhost:8080/api/events/location/${locationId}`).subscribe(events => {
      this.events = events;
    });
  }

  loadReviews(locationId: number) {
    this.http.get<any[]>(`http://localhost:8080/api/reviews/location/${locationId}`).subscribe(reviews => {
      this.reviews = reviews;
    });
  }

  submitReview() {
    if (!this.overallRating && !this.performanceRating && !this.soundLightRating && !this.spaceRating) {
      this.reviewError = 'Morate oceniti bar jednu stavku.';
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      this.reviewError = 'Morate biti prijavljeni da biste ostavili utisak.';
      return;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    const userEmail = payload.sub;

    const reviewData = {
      locationId: this.selectedLocation?.id,
      eventId: this.selectedEventId,
      userEmail: userEmail,
      rate: {
        performance: this.performanceRating,
        soundAndLighting: this.soundLightRating,
        venue: this.spaceRating,
        overallImpression: this.overallRating
      },
      comment: this.comment
    };

    this.http.post('http://localhost:8080/api/reviews', reviewData).subscribe({
      next: () => {
        alert('Utisak uspešno dodat!');
        this.loadReviews(this.selectedLocation!.id!);
        this.resetReviewForm();
      },
      error: err => {
        this.reviewError = err.error?.message || 'Greška pri dodavanju utiska.';
      }
    });
  }
}
