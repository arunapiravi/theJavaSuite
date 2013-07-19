import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;

import net.spy.memcached.internal.OperationFuture;

import com.couchbase.client.CouchbaseClient;

/*
 * Module that runs add operations
 */

public class Adds {
    public static void add_items (CouchbaseClient client, Variables V, String _prefix) throws JSONException {
        Random gen = new Random(123456789);
        List<OperationFuture<Boolean>> adds = new LinkedList<OperationFuture<Boolean>>();
        for (int i=V.getItemCount(); i<(V.getItemCount() + V.getAddCount()); i++) {
            OperationFuture<Boolean> addOp;
            String key = String.format("%s%d", _prefix, i);
            if (V.isJson()) {
                addOp = client.add(key, Gen.retrieveJSON(gen, V).toString());
            } else {
                addOp = client.add(key, Gen.retrieveBinary(V));
            }
            adds.add(addOp);
        }

        while (!adds.isEmpty()) {
            try {
                if (adds.get(0).get().booleanValue() == false) {
                    // TODO: Something I'd guess, as add failed
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            adds.remove(0);
        }
    }
}
