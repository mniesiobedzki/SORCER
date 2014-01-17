package sorcer.trigo.provider;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sorcer.service.Context;
import sorcer.service.ContextException;

/**
 * @author Macha
 */
@SuppressWarnings("rawtypes")
public interface Sin extends Remote {

    public Context sin(Context context) throws RemoteException, ContextException;

}
