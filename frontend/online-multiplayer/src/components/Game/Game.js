import React, { useEffect, useState } from 'react'
import Cookies from 'universal-cookie';
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { useNavigate, useParams } from 'react-router-dom';

let stompClient = null;

export const Game = () => {

  const cookies = new Cookies();

  const { gameId } = useParams();

  const auth = JSON.parse(sessionStorage.getItem("user"));

  const navigate = useNavigate();

  // Connect on page load.
  useEffect(() => {
    connect();
  }, []);

  const connect = () => {

    let socket = new SockJS(`http://localhost:8080/ws?token=${cookies.get("token")}`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {

      stompClient.debug("Connected! " + frame);

      stompClient.subscribe("/topic/game/" + gameId, (message) => {
        let data = JSON.parse(message.body);
        console.log(data);
      });
    });
  }

  const leave = () => {
    stompClient.disconnect();
    navigate("/lobby");
  }

//   const handleDisconnect = (data) => {
//     stompClient.disconnect();
//     navigate(`/game/${data?.gameId}`);
//   }

//   const handleJoin = (id) => {
//     stompClient.send(`/app/join/${id}`);
//   }

  return (
   <div>
    <h1>Game {gameId}</h1>
    <button onClick={leave}>Leave</button>
   </div>
  )
}
