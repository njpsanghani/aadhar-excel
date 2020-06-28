package app.tool;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;


public class Data {

    /*CREATE TABLE public.tbl_organization_master_dummy
(
    org_id integer NOT NULL DEFAULT nextval('tbl_organization_master_dummy_org_id_seq'::regclass),
    organizations_name text COLLATE pg_catalog."default" NOT NULL,
    address text COLLATE pg_catalog."default",
    mobile_no text COLLATE pg_catalog."default",
    pin_code_id integer NOT NULL DEFAULT 0,
    email_id text COLLATE pg_catalog."default",
    created_by integer NOT NULL DEFAULT 0,
    is_active boolean NOT NULL DEFAULT true,
    updated_by integer NOT NULL DEFAULT 0,
    is_deleted boolean NOT NULL DEFAULT false,
    created_date_time timestamp with time zone NOT NULL DEFAULT now(),
    modified_date_time timestamp with time zone NOT NULL DEFAULT now(),
    organization_type_id integer,
    CONSTRAINT tbl_organization_master_dummy_id PRIMARY KEY (org_id)
)*/


   @Getter @Setter @SerializedName("org_id") private int organizationId;
   @Getter @Setter @SerializedName("organizations_name") private String organizationName;
   @Getter @Setter @SerializedName("address") private String organizationAddress;
   @Getter @Setter @SerializedName("mobile_no") private String organizationMobileNo;
   @Getter @Setter @SerializedName("email_id") private String organizationEmailId;
   @Getter @Setter @SerializedName("organization_type_id") private int organizationTypeId;
   @Getter @Setter @SerializedName("organization_district_id") private int organizationDistrictId;

    /*CREATE TABLE public.tbl_branch_master
(
    branch_id integer NOT NULL DEFAULT nextval('tbl_branch_master_branch_id_seq'::regclass),
    branch_name text COLLATE pg_catalog."default" NOT NULL,
    pin_code_id integer,
    address text COLLATE pg_catalog."default",
    contact_no text COLLATE pg_catalog."default",
    ifsc_code text COLLATE pg_catalog."default",
    org_id integer NOT NULL DEFAULT 0,
    is_active boolean NOT NULL DEFAULT true,
    created_date_time timestamp with time zone NOT NULL DEFAULT now(),
    modified_date_time timestamp with time zone NOT NULL DEFAULT now(),
    is_main_branch boolean NOT NULL DEFAULT false,
    created_by integer NOT NULL DEFAULT 0,
    updated_by integer NOT NULL DEFAULT 0,
    is_deleted boolean NOT NULL DEFAULT false,
    district_id integer,
    CONSTRAINT tbl_branch_master_id PRIMARY KEY (branch_id)
)*/

   @Getter @Setter @SerializedName("branch_id") private int branchId;
   @Getter @Setter @SerializedName("branch_name") private String branchName;
   @Getter @Setter @SerializedName("pin_code_id") private int branchPincodeId;
   @Getter @Setter @SerializedName("branchAddress") private String branchAddress;
   @Getter @Setter @SerializedName("contact_no") private String branchMobileNo;
   @Getter @Setter @SerializedName("ifsc_code") private String branchIFSCCode;
   @Getter @Setter @SerializedName("district_id") private int branchDistrictId;
   @Getter @Setter @SerializedName("district_name") private String districtName;
   @Getter @Setter @SerializedName("branch_taluka") private String branchTaluka;
    /*CREATE TABLE public.tbi_users_dummy
(
    id integer NOT NULL DEFAULT nextval('tbi_users_dummy_id_seq'::regclass),
    name text COLLATE pg_catalog."default",
    address text COLLATE pg_catalog."default",
    mobile_no character varying(10) COLLATE pg_catalog."default",
    email_id character varying(256) COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default" NOT NULL,
    date_of_birth date,
    role_id integer,
    branch_id integer,
    updated_by integer,
    is_deleted boolean NOT NULL DEFAULT false,
    is_active boolean NOT NULL DEFAULT true,
    created_by integer NOT NULL DEFAULT 0,
    created_date_time timestamp with time zone NOT NULL DEFAULT now(),
    modified_date_time timestamp with time zone,
    last_login_date_time timestamp with time zone,
    username text COLLATE pg_catalog."default",
    is_allow_update_loan_status boolean NOT NULL DEFAULT false,
    org_id integer,
    last_log_out_time timestamp with time zone,
    fail_count integer NOT NULL DEFAULT 0,
    fail_time time without time zone,
    first_time_login boolean NOT NULL DEFAULT true,
    CONSTRAINT tbi_users_dummy_pkey PRIMARY KEY (id),
    CONSTRAINT tbi_users_dummy_username_key UNIQUE (username)
)*/

   @Getter @Setter @SerializedName("id") private int userId;
   @Getter @Setter @SerializedName("name") private String userName;
   @Getter @Setter @SerializedName("userAddress") private String userAddress;
   @Getter @Setter @SerializedName("userMobileNo") private String userMobileNo;
   @Getter @Setter @SerializedName("userEmailId") private String userEmailId;
   @Getter @Setter @SerializedName("password") private String userPassword;
   @Getter @Setter @SerializedName("role_id") private int userRoleId;
   @Getter @Setter @SerializedName("username") private String userUsername;

   @Getter @Setter @SerializedName("user_district_id") private int user_district_id;
}
