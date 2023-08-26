BEGIN^^

INSERT INTO public.user_permission VALUES (1, true, true, true, true, true) ON CONFLICT ON CONSTRAINT user_permission_pkey DO NOTHING^^
INSERT INTO public.user_permission VALUES (2, false, true, false, true, false) ON CONFLICT ON CONSTRAINT user_permission_pkey DO NOTHING^^
INSERT INTO public.user_permission VALUES (3, true, true, true, true, false) ON CONFLICT ON CONSTRAINT user_permission_pkey DO NOTHING^^
INSERT INTO public.user_permission VALUES (4, false, false, false, false, true) ON CONFLICT ON CONSTRAINT user_permission_pkey DO NOTHING^^

INSERT INTO public.role_permission VALUES ('ROLE_sie_admin', 4) ON CONFLICT ON CONSTRAINT role_permission_pkey DO NOTHING^^
INSERT INTO public.role_permission VALUES ('ROLE_operator', 2) ON CONFLICT ON CONSTRAINT role_permission_pkey DO NOTHING^^
INSERT INTO public.role_permission VALUES ('ROLE_engineer', 3) ON CONFLICT ON CONSTRAINT role_permission_pkey DO NOTHING^^
INSERT INTO public.role_permission VALUES ('ROLE_ADMIN', 1) ON CONFLICT ON CONSTRAINT role_permission_pkey DO NOTHING^^

ALTER TABLE public.user_audit ALTER COLUMN session_key TYPE character varying(2550)^^

COMMIT^^
