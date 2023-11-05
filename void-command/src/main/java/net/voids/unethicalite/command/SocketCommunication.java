package net.voids.unethicalite.command;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.unethicalite.api.game.Game;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Slf4j
public class SocketCommunication
{
    private static SocketCommunication socketCommunication;

    public static SocketCommunication getInstance()
    {
        if (socketCommunication == null)
        {
            socketCommunication = new SocketCommunication();
        }

        return socketCommunication;
    }


    private static final State state = State.getInstance();



    @Getter
    private Socket socket = IO.socket(URI.create("ws://localhost:49201/osrsplugin"));

    @Setter
    private int heartBeatInterval = 5000;

    private Timer timer;


    private SocketCommunication()
    {
        socket.connect();

        if (Game.getGameAccount() != null)
        {
            identify();
        }
    }


    public void identify()
    {

        if (Game.getGameAccount() != null && Game.getGameAccount().getUsername() != null)
        {
            try
            {
                JSONObject payload = new JSONObject();
                payload.put("username", Game.getGameAccount().getUsername());
                socket.emit("identify", payload);
            }
            catch (JSONException e)
            {

            }
        }
    }

    public void reportJobChanged()
    {
        socket.emit("ACTIVITY:JOB-CHANGED", state.getJobTitle());
    }

    public void reportTaskChanged()
    {
        socket.emit("ACTIVITY:TASK-CHANGED", state.getTaskStatus());
    }



    public void startHeartbeat()
    {
        if (timer == null)
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(heartbeat(), 0, heartBeatInterval);
        }
    }

    public void stopHeartbeat()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }


    private TimerTask heartbeat()
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                if (socket != null)
                {
                    socket.emit("heartbeat");
                }
            }
        };
    }
}
