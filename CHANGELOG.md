## MarketStock-1.0.0:
#### Added
- Add the Simulation class to facilitate the co-existence between threads and a user.
- Add the Croupier class to draw random values for Entities and Assets.
- Add builders for each concrete class.
- Add unit tests.
- Add documentation.
- Add Player creation through a dialog.

#### Fixed
- Serialization and deserialization works properly.
- GUI looks well on smaller screens.

#### Changed
- Simulation is serialized to a JSON file instead of a binary file.
- Random values for Entities and Assets are drawn by the Croupier class.
- Change the composition of an Index; introduce a new interface.
- Change the composition of Assets and Entities; introduce new interfaces.
