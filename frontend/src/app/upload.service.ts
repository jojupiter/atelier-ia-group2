import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private baseUrl = 'http://localhost:8080/api';

  video= new Object({"username":"sedou",
         "opacity":60,
         "videoname":"",
         "email":""      
})

  constructor(private http: HttpClient) { }

  pushFileToStorage(file: File): Observable<HttpEvent<{}>> {
    const data: FormData = new FormData();
    data.append('file', file);
    data.append('videoname', file.name);
    data.append('opacity', "89");
    data.append('username', "sedou");

    const newRequest = new HttpRequest('POST', 'http://localhost:8080/api/test/upload', data, {
    reportProgress: true,
    responseType: 'text'
    });
    return this.http.request(newRequest);
}
  

  testUpload():Observable<any[]> {
    
    return this.http.post<any[]>(`${this.baseUrl}/test/testupload`,this.video);
  }

  blurVideo(file: File,model:String):Observable<HttpEvent<{}>> {
    const data: FormData = new FormData();
    data.append('videoname', file.name);
    data.append('opacity', "89");
    data.append('username', "sedou");
    data.append('model', "1");

    const newRequest = new HttpRequest('POST', 'http://localhost:8080/api/test/blur', data, {
    reportProgress: true,
    responseType: 'text'
    });
    return this.http.request(newRequest);
  }


  downloadFile(filename: string): Observable<Blob> {
    return this.http.get('http://localhost:8080/api/test/download/' + filename, { responseType: 'blob' });
  }
}
