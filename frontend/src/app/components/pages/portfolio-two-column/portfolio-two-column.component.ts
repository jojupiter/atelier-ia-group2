import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UploadService } from 'src/app/upload.service';



interface Food {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-portfolio-two-column',
  templateUrl: './portfolio-two-column.component.html',
  styleUrls: ['./portfolio-two-column.component.scss']
})
export class PortfolioTwoColumnComponent implements OnInit {
  value: number = 50; // Valeur initiale du slider

  disabled = false;
  max = 100;
  min = 0;
  showTicks = false;
  step = 1;
  thumbLabel = false;
  selectedFood:Food={value: '1', viewValue: 'Model  without interview'};

  title = 'File-Upload-Save';
  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = { percentage: 0 };
  selectedFile = null;
  changeImage = false;
  status: boolean = false; // Déclarer la propriété status
  admincheck:boolean = false;

  foods: Food[] = [
    //{value: '0', viewValue: 'Model 0'},
    {value: '1', viewValue: 'Model  without interview'},
    {value: '2', viewValue: 'Model with interview'},
  ];
  

  constructor(private uploadService: UploadService) { }

  ngOnInit(): void {
    
  }

  downloadFile(){
    const link = document.createElement('a');
    link.setAttribute('target', '_blank');
    link.setAttribute('href', '_File_Saved_Path');
    link.setAttribute('download', 'file_name.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();
  }
  change($event) {
    this.changeImage = true;
  }
  changedImage(event) {
    this.selectedFile = event.target.files[0];
  }

  upload() {
    this.progress.percentage = 0;
    this.currentFileUpload = this.selectedFiles.item(0);
this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
         alert('Video Successfully Uploaded');
      }
      this.selectedFiles = undefined;
     }
    );
  }



  blur() {
    this.progress.percentage = 0;
    console.log(this.currentFileUpload,"   Select Model ",this.selectedFood.value)
    //this.currentFileUpload = this.selectedFiles.item(0);
this.uploadService.blurVideo(this.currentFileUpload,this.selectedFood.value).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
         alert('Video Successfully Blured');
      }
      this.selectedFiles = undefined;
     }
    );
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  
  downloadFile2(): void {
    console.log(this.selectedFile.name)
    this.uploadService.downloadFile(this.selectedFile.name).subscribe(blob => {
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = this.selectFile.name;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  }
  
}
