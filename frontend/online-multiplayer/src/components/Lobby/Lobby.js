import React, { useEffect, useState } from 'react'
import Cookies from 'universal-cookie';
import SockJS from 'sockjs-client';
import { useAuth } from '../../hooks/useAuth';
import {Stomp} from '@stomp/stompjs';

let stompClient = null;

export const Lobby = () => {

  const cookies = new Cookies();

  const { auth } = useAuth();

  const [gameData, setGameData] = useState([{}]);
  // const [isConnected, setConnected] = useState(false);

  // Connect on page load.
  useEffect(() => {
    connect();
  }, [])

  // useEffect(() => {

  // }, [gameData])

  const connect = () => {

    let socket = new SockJS(`http://localhost:8080/ws?token=${cookies.get("token")}`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
      stompClient.debug("Connected! " + frame);
      // setConnected(true);
      stompClient.subscribe("/topic/lobby/" + auth.id, (message) => {
        setGameData(JSON.parse(message.body));
      });
    });
  }

  return (
    <div>
      <h1>Lobby</h1>
      <div>
        {
          gameData?.map((item, index) => (
            <div key={index}>
              <p>{item?.maxPlayers}</p>
              <p>{item?.dateCreated}</p>
              <p>{item?.id}</p>
              <div>
                {
                  item?.players?.map((p, pindex) => (
                    <div key={pindex}>
                      <p>{p?.id}</p>
                      <p>{p?.username}</p>
                      <p>{p?.role}</p>
                      <p>{p?.dateCreated}</p>
                      <p>{p?.active}</p>
                      <p>{p?.isReported}</p>
                    </div>
                  ))
                }
              </div>
            </div>
          ))
        }
      </div>
    </div>
  )
}
