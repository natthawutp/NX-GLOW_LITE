# GWH_TM_KIT
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
| 18 | KIT_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | KIT_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | KIT_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | KIT_MPRD_COD | NVARCHAR2(100) | N | Product Code (Mother) / 商品コード(親) |
| 22 | KIT_MORG_COD | CHAR(2) | Y | Countru of Origin (Mother) / 原産国(親) |
| 23 | KIT_MPRD_QTY | NUMBER(12,3) | N | QTY (Mother) / 数量(親) |
| 24 | KIT_MSBI_COD | NVARCHAR2(40) | N | Sub Inventry (Mother) / 等級コード(親) |
| 25 | KIT_CPRD_COD | NVARCHAR2(100) | N | Product Code (Child) / 商品コード(子) |
| 26 | KIT_CORG_COD | CHAR(2) | Y | Countru of Origin (Child) / 原産国(子) |
| 27 | KIT_CPRD_QTY | NUMBER(12,3) | N | QTY (Child) / 数量(子) |
| 28 | KIT_CSBI_COD | NVARCHAR2(40) | N | Sub Inventry (Child) / 等級コード(子) |
| 29 | KIT_BOND_FLG | CHAR(1) | N | Bond Flag / 保税フラグ |
| 30 | KIT_KND | CHAR(2) | N | Kitting Type / Kitting区分 |
| 31 | KIT_NOKT_FLG | CHAR(1) | N | Kitting Prohibition Flag / 組立て禁止フラグ |
| 32 | KIT_RMKS | NVARCHAR2(1000) | Y | Remarks / 備考 |
