#!/bin/bash
set -e

# Check if the database exists
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
  DO
  \$\$
  BEGIN
     IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'owca') THEN
        PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE owca');
     END IF;
  END
  \$\$;
EOSQL