# GWH_TM_OTPK
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | OTPK_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | OTPK_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | OTPK_COD | NVARCHAR2(6) | N | Optimal Picking Key |
| 21 | OTPK_PRI_KND | NUMBER(1) | N | Optimal Priority Kind |
| 22 | OTPK_DFPK_KND | CHAR(1) | N | Default Picking Method |
| 23 | OTPK_RLPK_FLG | CHAR(1) | N | Relay Picking Method |
| 24 | OTPK_MTPK_FLG | CHAR(1) | N | Multi Picking Method |
| 25 | OTPK_RDPK_FLG | CHAR(1) | N | Rendezvous Picking Method |
| 26 | OTPK_MAXT_NUM | NUMBER(1) | Y | Maximum of tote for Multi Picking (Set 2-9) |
| 27 | OTPK_AREA_COD | CHAR(3) | Y | Storage Area/Aisle |
| 28 | OTPK_EX_KND | CHAR(1) | Y | Order Extraction Conditions |
| 29 | OTPK_EX_FROM_COD | NVARCHAR2(40) | Y | Order Extraction Range From |
| 30 | OTPK_EX_TO_COD | NVARCHAR2(40) | Y | Order Extraction Range To |
| 31 | OTPK_EX_PRI_KND | NUMBER(2) | Y | Order Extraction Priority |
| 32 | OTPK_EX_STS_KND | CHAR(1) | Y | Order Extraction Status |
| 33 | OTPK_FROM_AREA_COD | CHAR(3) | Y | Location Area From |
| 34 | OTPK_FROM_RACK_COD | NVARCHAR2(20) | Y | Location Rack From |
| 35 | OTPK_FROM_PSTN_COD | NVARCHAR2(6) | Y | Location Position From |
| 36 | OTPK_FROM_LVL_COD | NVARCHAR2(4) | Y | Location Level From |
| 37 | OTPK_TO_AREA_COD | CHAR(3) | Y | Location Area To |
| 38 | OTPK_TO_RACK_COD | NVARCHAR2(20) | Y | Location Rack To |
| 39 | OTPK_TO_PSTN_COD | NVARCHAR2(6) | Y | Location Position To |
| 40 | OTPK_TO_LVL_COD | NVARCHAR2(4) | Y | Location Level To |
| 41 | OTPK_MAXO_NUM | NUMBER(3) | Y | Maximum of order for Total Picking |
| 42 | OTPK_UINE_KND | CHAR(1) | Y | Unit to ignore on error |
