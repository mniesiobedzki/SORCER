package sorcer.trigo.provider;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sorcer.service.Context;
import sorcer.service.ContextException;

@SuppressWarnings("rawtypes")
public interface Cos extends Remote {

    public Context cos(Context context) throws RemoteException, ContextException;

}