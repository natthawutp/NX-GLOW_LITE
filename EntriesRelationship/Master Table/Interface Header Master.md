# GWH_TM_IF_H
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N |  |
| 2 | CRT_YMD | CHAR(8) | N |  |
| 3 | CRT_TIM | CHAR(6) | N |  |
| 4 | CRT_TMID | NVARCHAR2(200) | N |  |
| 5 | CRT_USER | NVARCHAR2(100) | N |  |
| 6 | CRT_PGM | NVARCHAR2(60) | N |  |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N |  |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N |  |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N |  |
| 10 | UPD_YMD | CHAR(8) | N |  |
| 11 | UPD_TIM | CHAR(6) | N |  |
| 12 | UPD_TMID | NVARCHAR2(200) | N |  |
| 13 | UPD_USER | NVARCHAR2(100) | N |  |
| 14 | UPD_PGM | NVARCHAR2(60) | N |  |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N |  |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N |  |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N |  |
| 18 | IFH_CPNY_COD | NVARCHAR2(12) | N |  |
| 19 | IFH_WHS_COD | NVARCHAR2(8) | N |  |
| 20 | IFH_CUST_COD | NVARCHAR2(26) | N |  |
| 21 | IFH_IF_CHL | NVARCHAR2(40) | N |  |
| 22 | IFH_IF_URL | NVARCHAR2(40) | N |  |
| 23 | IFH_IF_USR | NVARCHAR2(40) | N |  |
| 24 | IFH_IF_PWD | NVARCHAR2(40) | N |  |
| 25 | IFH_IF_PRT | NUMBER(5) | N |  |
| 26 | IFH_IF_KND | NVARCHAR2(10) | N |  |
| 27 | IFH_TRSF_KND | CHAR(1) | N |  |
| 28 | IFH_DAT_URL | NVARCHAR2(500) | N |  |
| 29 | IFH_DAT_TYP | NVARCHAR2(60) | N |  |
| 30 | IFH_FILE_TYP | NVARCHAR2(10) | N |  |
| 31 | IFH_SPT_COD | NVARCHAR2(2) | Y |  |
| 32 | IFH_LOC_FLD | NVARCHAR2(400) | N |  |
| 33 | IFH_IF_FLD | NVARCHAR2(400) | Y |  |
| 34 | IFH_USR_COD | NVARCHAR2(100) | N |  |
| 35 | IFH_DT_FMT | NVARCHAR2(100) | N |  |
| 36 | IFH_LNG_COD | NVARCHAR2(30) | N |  |
| 37 | IFH_BAK_FLD | NVARCHAR2(400) | Y |  |
| 38 | IFH_FILE_NAM | NVARCHAR2(100) | Y |  |
| 39 | IFH_DESC | NVARCHAR2(4000) | Y |  |
| 40 | IFH_HDR_FLG | CHAR(1) | Y |  |
| 41 | IFH_MAIL_ADR | NVARCHAR2(256) | Y |  |
