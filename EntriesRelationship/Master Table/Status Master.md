# GWH_TM_STS
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
| 18 | STS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | STS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | STS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | STS_BSNS_COD | NVARCHAR2(4) | N | Busines Code / 入出荷区分 |
| 22 | STS_COD | CHAR(3) | N | Status Code / ステータスコード |
| 23 | STS_LBL_COD | NVARCHAR2(200) | N | Label Code / ラベルコード |
| 24 | STS_WKST_KND | CHAR(1) | N | Work State Kind / 作業状態区分 |
| 25 | STS_RMKS | NVARCHAR2(100) | Y | Remarks / 備考 |
| 26 | STS_SHU_FLG | CHAR(1) | N | Shuttle send Flag / SHUTTLE送信フラグ |
| 27 | STS_CFM_TMG_FLG | CHAR(1) | N | Confirm timing Flag / 確定タイミングフラグ |
