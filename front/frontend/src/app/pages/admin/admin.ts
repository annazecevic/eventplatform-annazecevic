import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

interface AccountRequest {
  email: string;
  address: string;
  status: string;
  createdAt: string;
  rejectionReason?: string;
}

interface Event {
  id?: number;
  name: string;
  type: string;
  address: string;
  date: string;
  price: number;
  locationId: number;
}

interface Location {
  id?: number;
  name: string;
  address: string;
  type: string;
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin.html',
  styleUrls: ['./admin.scss']
})
export class AdminComponent implements OnInit {

  // === Account Requests ===
  requests: AccountRequest[] = [];
  selectedEmail: string | null = null;
  rejectForm: FormGroup;

  // === Events ===
  events: Event[] = [];
  eventForm: FormGroup;
  editingEvent: Event | null = null;

  // === Locations ===
  locations: Location[] = [];
  locationForm: FormGroup;
  editingLocation: Location | null = null;

  constructor(private http: HttpClient, private fb: FormBuilder) {
    this.rejectForm = this.fb.group({ reason: [''] });
    this.eventForm = this.fb.group({
      name: [''],
      type: [''],
      address: [''],
      date: [''],
      price: [''],
      locationId: ['']
    });
    this.locationForm = this.fb.group({
      name: [''],
      address: [''],
      type: ['']
    });
  }

  ngOnInit(): void {
    this.loadRequests();
    this.loadEvents();
    this.loadLocations();
  }

  // =======================
  // ACCOUNT REQUESTS
  // =======================
  loadRequests() {
    this.http.get<AccountRequest[]>('http://localhost:8080/api/account-requests')
      .subscribe({
        next: (data) => this.requests = data,
        error: (err) => console.error('Failed to load requests:', err)
      });
  }

  acceptRequest(email: string) {
    const encodedEmail = encodeURIComponent(email);
    this.http.post(`http://localhost:8080/api/account-requests/${encodedEmail}/accept`, {}, { responseType: 'text' })
      .subscribe({
        next: () => {
          alert('User approved successfully');
          this.loadRequests();
        },
        error: (err) => console.error('Error accepting request', err)
      });
  }

  openReject(email: string) {
    this.selectedEmail = email;
  }

  reject() {
    if (!this.selectedEmail) return;
    const reason = this.rejectForm.value.reason || 'Rejected by admin';
    this.http.post(
      `http://localhost:8080/api/account-requests/${this.selectedEmail}/reject?reason=${encodeURIComponent(reason)}`,
      {},
      { responseType: 'text' }
    ).subscribe({
      next: () => {
        alert('User rejected');
        this.loadRequests();
        this.cancelReject();
      },
      error: (err) => console.error('Error rejecting request', err)
    });
  }

  cancelReject() {
    this.selectedEmail = null;
    this.rejectForm.reset();
  }

  // =======================
  // EVENTS
  // =======================
  loadEvents() {
    this.http.get<Event[]>('http://localhost:8080/api/events')
      .subscribe({
        next: (data) => this.events = data,
        error: (err) => console.error('Failed to load events:', err)
      });
  }

  saveEvent() {
    const event = this.eventForm.value;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    if (this.editingEvent) {
      this.http.put(`http://localhost:8080/api/events/${this.editingEvent.id}`, event, { headers })
        .subscribe({
          next: () => {
            alert('Event updated');
            this.loadEvents();
            this.cancelEventEdit();
          },
          error: (err) => console.error('Error updating event', err)
        });
    } else {
      this.http.post('http://localhost:8080/api/events', event, { headers })
        .subscribe({
          next: () => {
            alert('Event created');
            this.loadEvents();
            this.eventForm.reset();
          },
          error: (err) => console.error('Error creating event', err)
        });
    }
  }

  editEvent(ev: Event) {
    this.editingEvent = ev;
    this.eventForm.patchValue(ev);
  }

  deleteEvent(id: number) {
    if (confirm('Delete this event?')) {
      this.http.delete(`http://localhost:8080/api/events/${id}`)
        .subscribe({
          next: () => {
            alert('Event deleted');
            this.loadEvents();
          },
          error: (err) => console.error('Error deleting event', err)
        });
    }
  }

  cancelEventEdit() {
    this.editingEvent = null;
    this.eventForm.reset();
  }

  // =======================
  // LOCATIONS
  // =======================
  loadLocations() {
    this.http.get<Location[]>('http://localhost:8080/api/locations')
      .subscribe({
        next: (data) => this.locations = data,
        error: (err) => console.error('Failed to load locations:', err)
      });
  }

  saveLocation() {
    const location = this.locationForm.value;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    if (this.editingLocation) {
      this.http.put(`http://localhost:8080/api/locations/${this.editingLocation.id}`, location, { headers })
        .subscribe({
          next: () => {
            alert('Location updated');
            this.loadLocations();
            this.cancelLocationEdit();
          },
          error: (err) => console.error('Error updating location', err)
        });
    } else {
      this.http.post('http://localhost:8080/api/locations', location, { headers })
        .subscribe({
          next: () => {
            alert('Location created');
            this.loadLocations();
            this.locationForm.reset();
          },
          error: (err) => console.error('Error creating location', err)
        });
    }
  }

  editLocation(loc: Location) {
    this.editingLocation = loc;
    this.locationForm.patchValue(loc);
  }

  deleteLocation(id: number) {
    if (confirm('Delete this location?')) {
      this.http.delete(`http://localhost:8080/api/locations/${id}`)
        .subscribe({
          next: () => {
            alert('Location deleted');
            this.loadLocations();
          },
          error: (err) => console.error('Error deleting location', err)
        });
    }
  }

  cancelLocationEdit() {
    this.editingLocation = null;
    this.locationForm.reset();
  }
}
