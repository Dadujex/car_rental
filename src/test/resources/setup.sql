DROP TABLE IF EXISTS public.cars CASCADE;
DROP TABLE IF EXISTS public.orders CASCADE;

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

INSERT INTO public.cars(name, active, image, price)
VALUES ('Audi', true, null, 12.2);

INSERT INTO public.cars(name, active, image, price)
VALUES ('Ford', false, null, 21.1);

INSERT INTO public.cars(name, active, image, price)
VALUES ('BMW', true, null, 21.1);

INSERT INTO public.orders(name, email, address, mobile, number_of_days, total_price, start_date, car_id, end_date)
VALUES ('Gabor', 'email', 'addr', '+36', 3, 36.6,
        '2024-10-10', 1, '2024-10-11');

INSERT INTO public.orders(name, email, address, mobile, number_of_days, total_price, start_date, car_id, end_date)
VALUES ('Andras', 'email', 'addr', '+36', 1, 12.2,
        current_date, 1, (CURRENT_DATE + INTERVAL '1 day'));

INSERT INTO public.orders(name, email, address, mobile, number_of_days, total_price, start_date, car_id, end_date)
VALUES ('Dominik', 'email', 'addr', '+36', 2, 42.2,
        '2025-10-10', 2, '2025-10-11');