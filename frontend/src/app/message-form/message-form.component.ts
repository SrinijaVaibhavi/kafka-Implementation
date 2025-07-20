import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-message-form',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './message-form.component.html',
  styleUrls: ['./message-form.component.css']
})
export class MessageFormComponent implements OnInit {
  firstName = '';
  lastName = '';
  email = '';
  subject = '';
  message = '';
  file: File | null = null;

  statusMsg = '';
  statusMsgColor: 'success' | 'error' | '' = '';

  constructor(private messageService: MessageService) {}

  ngOnInit() {
    // Test message on init to check UI binding
    this.statusMsg = 'Ready to send your message!';
    this.statusMsgColor = 'success';
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0] || null;
  }

  sendMessage() {
    this.statusMsg = '';
    this.statusMsgColor = '';

    if (!this.firstName || !this.lastName || !this.email || !this.subject || !this.message) {
      this.statusMsg = 'Please fill out all fields!';
      this.statusMsgColor = 'error';
      return;
    }

    const formData = new FormData();
    formData.append('firstName', this.firstName);
    formData.append('lastName', this.lastName);
    formData.append('email', this.email);
    formData.append('subject', this.subject);
    formData.append('message', this.message);
    if (this.file) {
      formData.append('attachment', this.file);
    }

    this.messageService.sendMessage(formData).subscribe({
      next: () => {
        console.log('Success callback triggered');
        this.statusMsg = 'Message sent successfully!';
        this.statusMsgColor = 'success';

        // Reset form fields
        this.firstName = '';
        this.lastName = '';
        this.email = '';
        this.subject = '';
        this.message = '';
        this.file = null;

        // Reset file input visually
        const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
        if (fileInput) fileInput.value = '';
      },
      error: (err) => {
        console.error('Error sending message:', err);
        this.statusMsg = 'Failed to send message.';
        this.statusMsgColor = 'error';
      }
    });
  }
}
