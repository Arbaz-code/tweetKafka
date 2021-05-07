import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth.service';
import { User } from 'src/app/interfaces/user';
@Component({
  selector: 'app-reset-pass',
  templateUrl: './reset-pass.component.html',
  styleUrls: ['./reset-pass.component.css']
})
export class ResetPassComponent implements OnInit {
  resetSubmitted: boolean;
  loading: boolean;

  constructor( private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router) { }
  resetPasswordForm: FormGroup;
  submitted: boolean = false;
  resetPasswordValue: string;
  currentUser: User;
  invalid = false;
  passwordResetComplete: boolean = false;
  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.resetPasswordForm = this.formBuilder.group({
      password: ['', [Validators.required,Validators.minLength(6)]],
    });
  }
  get f() {
    return this.resetPasswordForm.controls;
  }
  getPassword() {
    return this.resetPasswordForm.get('password').value;
  }
  onSubmit() {
    console.log('Submitted');
    this.submitted = true;
    if (this.resetPasswordForm.invalid) {
      return;
    }
    this.loading = true;
    this.authService
      .resetPassword( this.currentUser.username,this.getPassword())
        .subscribe((data: any) => {
          this.loading=false;
          if (
            (data.resetStatus !== undefined || data.resetStatus !== null) &&
            data.resetStatus == 'success'
          ) {
            
           if (confirm("Password reset done please login again")){
              this.authService.setCurrentUser(null);
            }
            this.authService.setCurrentUser(null);
            this.router.navigate([''])
          }
          this.passwordResetComplete = true;
        });
  }
}
