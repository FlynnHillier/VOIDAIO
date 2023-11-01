package net.voids.unethicalite.voidaio.login;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.unethicalite.api.account.GameAccount;
import net.unethicalite.api.input.Keyboard;
import net.unethicalite.client.Static;
import net.unethicalite.api.game.Game;

@Slf4j
public class Login
{
    public int login()
    {
        Client client = Static.getClient();
        int loginState = client.getLoginIndex();
        GameAccount gameAccount = Game.getGameAccount();
        if (gameAccount == null)
        {
            log.warn("No account set, stopping login event.");
            return -1000;
        }

        switch (loginState)
        {
            case State.AUTHENTICATOR:
                if (gameAccount.getAuth() != null)
                {
//                    totp = new Totp(gameAccount.getAuth());
                    log.error("totp expected here, what is this?");
                }
                else
                {
                    log.error("We are on auth screen, but auth code was not set.");
                    return -1000;
                }

//                client.setOtp(totp.now());
//                Keyboard.sendEnter();
                return 1000;

            case State.ENTER_CREDENTIALS:
                if (Game.getState() == GameState.LOGGING_IN)
                {
                    return 1000;
                }

                client.setUsername(gameAccount.getUsername());
                client.setPassword(gameAccount.getPassword());
                Keyboard.sendEnter();
                Keyboard.sendEnter();
                return 1000;

            case State.MAIN_MENU:
            case State.ACCEPT_TOS:
            case State.BEEN_DISCONNECTED:
                client.setLoginIndex(State.ENTER_CREDENTIALS);
                return 1000;

            case State.DISABLED:
            case State.INVALID_CREDENTIALS:
                log.warn("Invalid account set.");
                return 5000;

            default:
                log.warn("Cannot handle login state {}", loginState);
                return 1000;
        }
    }
    public interface State
    {
        int MAIN_MENU = 0;
        int BETA_WORLD = 1;
        int ENTER_CREDENTIALS = 2;
        int INVALID_CREDENTIALS = 3;
        int AUTHENTICATOR = 4;
        int ACCEPT_TOS = 12;
        int DISABLED = 14;
        int BEEN_DISCONNECTED = 24;
    }
}
