import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Event {
  id: number;
  name: string;
  address: string;
  type: string;
  date: string;
  price: number;
  recurrent: boolean;
  location?: any;
}

@Injectable({ providedIn: 'root' })
export class EventService {
  private apiUrl = 'http://localhost:8080/api/events/today';

  constructor(private http: HttpClient) {}

  getEventsToday(): Observable<Event[]> {
    return this.http.get<Event[]>(this.apiUrl);
  }

  searchEvents(filters: { name?: string; type?: string; address?: string; maxPrice?: number }): Observable<Event[]> {
    const params: any = {};
    if (filters.name) params.name = filters.name;
    if (filters.type) params.type = filters.type;
    if (filters.address) params.address = filters.address;
    if (filters.maxPrice !== undefined && filters.maxPrice !== null) params.maxPrice = filters.maxPrice ?? 0;
    return this.http.get<Event[]>('http://localhost:8080/api/events/search', { params });
  }
}
