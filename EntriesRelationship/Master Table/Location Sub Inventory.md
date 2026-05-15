# GWH_TM_LOC_SBIV
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
| 18 | LCSB_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | LCSB_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | LCSB_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | LCSB_LOC_COD | NVARCHAR2(36) | N | Location / ロケーション |
| 22 | LCSB_AREA_COD | CHAR(3) | N | Area Code / エリア |
| 23 | LCSB_RACK_COD | NVARCHAR2(20) | Y | Rack Code / ラック |
| 24 | LCSB_PSTN_COD | NVARCHAR2(6) | Y | Position Code / ポジション |
| 25 | LCSB_LVL_COD | NVARCHAR2(4) | Y | Level Code / レベル |
| 26 | LCSB_ZONE_COD | NVARCHAR2(4) | Y | Zone Code / ゾーン |
| 27 | LCSB_LOC_SBIV | NVARCHAR2(40) | Y | Sub Inventry / 等級コード |
