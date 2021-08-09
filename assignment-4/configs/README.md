
# Explanations

Each config4.X drives different simulations for the programming assignment 4.

All simulations end after 39 seconds before calling ``dumpappstate()`` and ``dumppacketstats()``.

All networks have two sender application nodes and two receiving application nodes.


## config 4.1

A RING network interconnecting senders and receivers

NO filter

NO drop_duplicates

## config 4.2

A MESH network interconnecting senders and receivers

NO filter

NO drop_duplicates

## config 4.3

A TREE network interconnecting senders and receivers

NO filter

NO drop_duplicates

## config 4.4

A TREE network interconnecting senders and receivers

With filtering by the reverse path activated

NO drop_duplicates

## config 4.5

A MESH network interconnecting senders and receivers

With filtering by the reverse path activated and detection and dropping of duplicates

