import React, { useEffect, useState } from 'react'
import Cookies from 'universal-cookie';
import SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { useNavigate } from 'react-router-dom';

let stompClient = null;

export const Lobby = () => {

  const cookies = new Cookies();

  const auth = JSON.parse(sessionStorage.getItem("user"));

  const [gameData, setGameData] = useState([]);

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

      stompClient.subscribe("/topic/lobby/" + auth.id, (message) => {
        let data = JSON.parse(message.body);
        if(data?.canJoin != null) {
          data?.canJoin ? handleDisconnect(data) : console.log("Can't join.");
        } else if (data?.redirectURL != null) {
          stompClient.disconnect();
          navigate(data?.redirectURL);
        } else {
          setGameData(JSON.parse(message.body));
        }
      });
    });
  }

  const handleDisconnect = (data) => {
    stompClient.disconnect();
    navigate(`/game/${data?.gameId}`);
  }

  const handleJoin = (id) => {
    stompClient.send(`/app/join/${id}`);
  }

  return (
    <div className="flex flex-col items-center">
      <h1 className="font-bold">Lobby</h1>
      <div className="flex flex-col gap-7">
        {
          gameData?.map((item, index) => (
            <div className="border-2 border-gray-100 shadow-lg rounded-lg flex gap-4 p-4" key={index}>
              <p>Players: {item?.players?.length}/{item?.maxPlayers}</p>
              <p>{item?.dateCreated}</p>
              <div>
                {
                  item?.players?.map((p, pindex) => (
                    <div key={pindex}>
                      <p>{p?.username}</p>
                    </div>
                  ))
                }
              </div>
              <button onClick={() => handleJoin(item?.id)} className="bg-green-300 rounded-lg px-4 py-2 hover:bg-green-500">Join</button>
            </div>
          ))
        }
      </div>
    </div>
  )
}
