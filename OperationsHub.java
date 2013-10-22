import com.couchbase.client.CouchbaseClient;


public class OperationsHub {
	public static void runClientOperations(final CouchbaseClient client, final Variables V,
			final String _prefix) throws InterruptedException {

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
}
