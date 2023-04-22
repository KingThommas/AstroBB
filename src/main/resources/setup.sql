CREATE TABLE IF NOT EXISTS players
(
    uuid CHAR(36) PRIMARY KEY,
    first_played DATETIME NOT NULL,
    last_played DATETIME NOT NULL,
    lobby_playtime BIGINT NOT NULL,
    level INT NOT NULL,
    exp INT NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    slot VARCHAR(255) NOT NULL,
    price INT NOT NULL
);

CREATE TABLE IF NOT EXISTS inventories
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid CHAR(36) NOT NULL,
    item_id INT NOT NULL,
    selected BOOLEAN NOT NULL,
    FOREIGN KEY (player_uuid) REFERENCES players(uuid),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS block_battles_ranks
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    rank INT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    rating INT NOT NULL
);

CREATE TABLE IF NOT EXISTS block_battles_stats
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid CHAR(36) NOT NULL,
    games_played INT NOT NULL,
    games_won INT NOT NULL,
    games_lost INT NOT NULL,
    last_game BOOLEAN NOT NULL,
    playtime BIGINT NOT NULL,
    total_moves INT NOT NULL,
    rank INT NOT NULL,
    rank_points INT NOT NULL,
    pass_level INT NOT NULL,
    pass_exp INT NOT NULL,
    pass_claimed INT NOT NULL,
    FOREIGN KEY (player_uuid) REFERENCES players(uuid),
    FOREIGN KEY (rank) REFERENCES block_battles_ranks(rank)
);

CREATE TABLE IF NOT EXISTS skulk_shopping_stats
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_uuid CHAR(36) NOT NULL,
    games_played INT NOT NULL,
    playtime BIGINT NOT NULL,
    FOREIGN KEY (player_uuid) REFERENCES players(uuid)
);