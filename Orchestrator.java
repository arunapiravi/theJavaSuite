import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;


public class Orchestrator {

    private static String[] _Nodes = {};		//Servers, Format: server1_ip,server2_ip..
    private static String _Port = "";			//Couchbase port
    private static String[] _buckets = {};		//Buckets, Format: bucket1:bucket1passwd,bucket2:bucket2passwd..
    private static String _prefix = "";			//Key prefixes
    private static boolean _json = false;
    private static int _itemCount = 0;
    private static int _itemSize = 0;
    private static double _setRatio = 0.0;
    private static double _appendRatio = 0.0;
    private static int _appendSize = 20;		//20 bytes default
    private static double _prependRatio = 0.0;
    private static int _prependSize = 20;		//20 bytes default
    private static double _delRatio = 0.0;
    private static double _expRatio = 0.0;
    private static int _expiration = 600;		//10 minutes default
    private static int _addCount = 0;
    private static boolean _loop = false;       //to continuously loop the load

    public static void main(String args[]) throws InterruptedException {

        try {
            File file = new File("test.properties");
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            parse_input(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Variables V = new Variables (_json, _itemCount, _itemSize, _setRatio,
                _appendRatio, _appendSize, _prependRatio, _prependSize,
                _delRatio, _expRatio, _expiration, _addCount);

        int _x_ = 0;

        while (_x_ < 1) {

            Thread[] _control = new Thread[_buckets.length];
            for (int i=0; i<_buckets.length; i++) {
                String bucketname = _buckets[i].split(":")[0];
                String bucketpasswd = "";
                if (_buckets[i].contains(":"))
                    bucketpasswd = _buckets[i].split(":")[1];

                final CouchbaseClient client = connect(_Nodes[0], bucketname, bucketpasswd);

                Runnable _control_ = new Runnable() {
                    public void run() {
                        // System.out.println("Client thread starts");
                        try {
                            runClientOperations(client, V);
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                    }
                };

                _control[i] = new Thread(_control_);
                _control[i].start();

            }

            for (int j=0; j<_buckets.length; j++) {
                _control[j].join();
            }

            if (!_loop)
                _x_++;

        }

        System.out.println(" ........... done ...........");
        System.exit(0);

    }

    private static void runClientOperations(final CouchbaseClient client, final Variables V)
                                    throws InterruptedException {

        Runnable _sets_ = new Runnable() {
            public void run() {
                // System.out.println("Sets thread starts");
                try {
                    Sets.set_items(client, V, _prefix);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };

        Runnable _gets_ = new Runnable() {
            public void run() {
                // System.out.println("Gets thread starts");
                try {
                    Gets.get_items(client, V, _prefix);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };

        Runnable _mods_ = new Runnable() {
            public void run() {
                // System.out.println("Mods thread starts");
                try {
                    Mods.mod_items(client, V, _prefix);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };

        Runnable _adds_ = new Runnable() {
            public void run() {
                // System.out.println("Adds thread starts");
                try {
                    Adds.add_items(client, V, _prefix);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };

        Runnable _dels_ = new Runnable() {
            public void run() {
                // System.out.println("Deletes thread starts");
                try {
                    Deletes.del_items(client, V, _prefix);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        };

        Thread _sets = new Thread(_sets_);
        Thread _gets = new Thread(_gets_);
        Thread _mods = new Thread(_mods_);
        Thread _adds = new Thread(_adds_);
        Thread _dels = new Thread(_dels_);

        _sets.start();      //Begin sets thread
        _gets.start();      //Begin gets thread
        _sets.join();       //Wait for sets thread to complete
        _dels.start();      //Begin deletes thread
        _mods.start();      //Begin mods thread
        _adds.start();      //Begin adds thread
        _dels.join();       //Wait for deletes thread to complete
        _adds.join();       //Wait for adds thread to complete
        _mods.join();       //Wait for mods thread to complete
        _gets.join();       //Wait for gets thread to complete
        client.shutdown();

    }

    private static CouchbaseClient connect(String _addr, String _bucketName, String _bucketPasswd) {
        /*
         * CouchbaseClient Connection to bucket
         */
        List<URI> uris = new LinkedList<URI>();
        uris.add(URI.create(String.format("http://" + _addr + ":" + _Port + "/pools")));
        CouchbaseConnectionFactoryBuilder cfb = new CouchbaseConnectionFactoryBuilder();
        try {
            return new CouchbaseClient(cfb.buildCouchbaseConnection(uris, _bucketName, _bucketPasswd));
        } catch (Exception e) {
            System.err.println("Error connecting to Couchbase: "
                    + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    private static void parse_input(Properties properties) {
        /*
         * Read test variables from test.properties file
         */
        Enumeration<Object> enuKeys = properties.keys();
        while(enuKeys.hasMoreElements()){
            String key = (String) enuKeys.nextElement();
            if (key.equals("nodes"))
                _Nodes = properties.getProperty(key).split(",");
            if (key.equals("port"))
                _Port = properties.getProperty(key);
            if (key.equals("buckets"))
                _buckets = properties.getProperty(key).split(",");
            if (key.equals("prefix"))
                _prefix = properties.getProperty(key);

            if (key.equals("json"))
                _json = (Boolean.parseBoolean(properties.getProperty(key)));
            if (key.equals("item-count"))
                _itemCount = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("item-size"))
                _itemSize = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("set-ratio"))
                _setRatio = (Double.parseDouble(properties.getProperty(key)));
            if (key.equals("append-ratio"))
                _appendRatio = (Double.parseDouble(properties.getProperty(key)));
            if (key.equals("append-size"))
                _appendSize = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("prepend-ratio"))
                _prependRatio = (Double.parseDouble(properties.getProperty(key)));
            if (key.equals("prepend-size"))
                _prependSize = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("del-ratio"))
                _delRatio = (Double.parseDouble(properties.getProperty(key)));
            if (key.equals("exp-ratio"))
                _expRatio = (Double.parseDouble(properties.getProperty(key)));
            if (key.equals("expiration"))
                _expiration = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("add-count"))
                _addCount = (Integer.parseInt(properties.getProperty(key)));
            if (key.equals("loop"))
                _loop = (Boolean.parseBoolean(properties.getProperty(key)));
        }
    }
}
