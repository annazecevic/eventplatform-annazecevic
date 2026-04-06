import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Location } from './location.model';

@Injectable({ providedIn: 'root' })
export class LocationsService {
  private apiUrl = 'http://localhost:8080/api/locations';

  constructor(private http: HttpClient) {}

  getAllLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl);
  }

  searchLocations(filters: { name?: string; address?: string; type?: string }): Observable<Location[]> {
    const params: any = {};
    if (filters.name) params.name = filters.name;
    if (filters.address) params.address = filters.address;
    if (filters.type) params.type = filters.type;
    return this.http.get<Location[]>(this.apiUrl + '/search', { params });
  }
}
