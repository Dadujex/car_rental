CREATE TABLE IF NOT EXISTS public.cars
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    name character varying(40) NOT NULL,
    active boolean NOT NULL,
    image character varying(30),
    price double precision NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.orders
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    name character varying(40) NOT NULL,
    email character varying(40) NOT NULL,
    address text NOT NULL,
    mobile character varying(20) NOT NULL,
    number_of_days integer NOT NULL,
    total_price double precision NOT NULL,
    start_date date NOT NULL,
    car_id integer NOT NULL,
    end_date date NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_car FOREIGN KEY(car_id) REFERENCES cars(id) ON DELETE CASCADE
);