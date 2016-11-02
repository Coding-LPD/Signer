import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import * as io from 'socket.io-client';

import { API } from './api';

@Injectable()
export class SocketService {

  socket: SocketIOClient.Socket;

  get(name: string): Observable<any> {
    this.socket = io.connect(this.getDomain() + '/' + name);
    this.socket.on('connect', () => this.connect());
    this.socket.on('disconnect', () => this.disconnect());
    this.socket.on('error', (err: any) => console.log(err));

    return Observable.create((observer: Subscriber<any>) => {
      this.socket.on('sign', (data: any) => observer.next(data) );
      this.socket.on('list', (data: any) => observer.next(data));
    });
  }

  assent(data: any) {
    this.socket.emit('assent', data);
  }

  refuse(data: any) {
    this.socket.emit('refusal', data);
  }

  private connect() {
    console.log('connect');
    this.socket.emit('list', 'I want list');
  }

  private disconnect() {
    console.log('disconnect');
  }

  private getDomain(): string {
    var index = API.domain.lastIndexOf('/');
    return API.domain.substring(0, index);    
  }

}