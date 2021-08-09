
***Explanations

Each config4.X by a correct solution of the programming assignment 4

Config 4.1

A RING network interconnecting senders and receivers

NO filter

NO drop_duplicates

Senders send 3 packets and receive many duplicates

MANY duplicates in the network

MANY packets dropped when their TTL would reach 0

Config 4.2

A MESH network interconnecting senders and receivers

NO filter

NO drop_duplicates

The number of duplicates is so high that the network collapses with a broadcast storm. Abort the CNSS (^C) otherwise it will crash by lack of space for the packets in waiting queues (heap exhausted).

Config 4.3

A TREE network interconnecting senders and receivers

NO filter

NO drop_duplicates

Senders send 3 packets and receive 3 packets

Some useless packets in the network dropped when they get to nodes that only have one interface and are not the destination node. As there are no loops, no duplicates are introduced, only useless packets

NO packets dropped when their TTL would reach 0

Config 4.4

A TREE network interconnecting senders and receivers

With filtering by the reverse path activated

NO drop_duplicates

Senders send 3 packets and receive 3 packets

After the first round of send / received packets the number of useless packets drops to 0 since packets are sent using the best path towards their destination.

NO packets dropped when their TTL would reach 0

Config 4.5

A MESH network interconnecting senders and receivers

With filtering by the reverse path activated and detection and dropping of duplicates

Senders send 3 packets and receive 3 packets

The routing of packets using flooding behaves in a similar way in the mesh network as in the tree network since all duplicates are immediately dropped.

NO packets dropped when their TTL would reach 0.
