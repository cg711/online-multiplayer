import React, { useEffect } from 'react'
import Cookies from 'universal-cookie';
import SockJS from 'sockjs-client';
import { useAuth } from '../../hooks/useAuth';
import {Stomp} from '@stomp/stompjs';

let stompClient = null;

export const Lobby = () => {

  const cookies = new Cookies();

  useEffect(() => {
    connect();
  }, [])

  const connect = () => {

    let socket = new SockJS(`http://localhost:8080/ws?token=${cookies.get("token")}`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      stompClient.debug("Connected!");
      console.log(frame);
    });
  }


  return (
    <div>Lobby</div>
  )
}
