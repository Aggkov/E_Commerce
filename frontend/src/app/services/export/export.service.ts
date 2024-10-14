import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../enviroments/enviroment";

@Injectable({
  providedIn: 'root'
})
export class ExportService {

  private readonly exportUrl = environment.coreServiceUrl + '/products';

  constructor(private httpClient: HttpClient) {
  }

  export(format: string) {
    const searchUrl = `${this.exportUrl}/admin/export?type=${format}`;
    // Make an HTTP GET request to the backend to export the file
    this.httpClient.get<Blob>(searchUrl, { observe: 'response', responseType: 'blob' as 'json'}) // 'blob' response type for binary file
      .subscribe({
        next: (response) => {
          const contentDisposition = response.headers.get('Content-Disposition');
          const filename = this.getFilenameFromContentDisposition(contentDisposition);

          // Call the method to trigger the file download
          if (response.body) {
            // Only proceed if response.body is not null
            this.downloadFile(response.body, filename);
          }
        },
        error: error => {
          console.error('Error during file export', error);
        }
      });
  }

  private getFilenameFromContentDisposition(contentDisposition: string | null): string {
    // if (!contentDisposition) {
    //   return 'export.unknown';  // Fallback if no filename is found
    // }

    const filenameMatch = contentDisposition?.match(/filename[^;=\n]*=["']?([^"';\n]*)["']?/);
    return filenameMatch ? filenameMatch[1] : 'export.unknown';
  }

  // Method to trigger file download
  private downloadFile(data: Blob, filename: string) {
    const blob = new Blob([data], { type: this.getMimeType(filename) });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;  // Use the filename extracted from headers
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }

  // Method to determine the MIME type based on format
  private getMimeType(format: string): string {
    switch (format) {
      case 'excel':
        return 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'; // Excel MIME type
      case 'csv':
        return 'text/csv';
      case 'json':
        return 'application/json';
      case 'yaml':
        return 'application/x-yaml';
      default:
        return 'application/octet-stream'; // Default binary MIME type
    }
  }
}
