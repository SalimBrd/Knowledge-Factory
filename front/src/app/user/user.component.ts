import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from '@app/models/user';
import { UserService } from '@app/services/user.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  loading = false;
  users: User[];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loading = true;

    var params = new HttpParams().set("page", "1").set("limit", "10");

    this.userService.getAll(params).pipe(first()).subscribe(users => {
      this.loading = false;
      this.users = users;
    });
  }

}
