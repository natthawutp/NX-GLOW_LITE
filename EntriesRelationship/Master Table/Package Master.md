# GWH_TM_PCM
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
| 18 | PCM_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | PCM_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | PCM_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | PCM_COD | NVARCHAR2(6) | N | Packing Material Code / 資材コード |
| 22 | PCM_NAM | NVARCHAR2(100) | Y | Packing Material Name / 梱包材名称 |
| 23 | PCM_LEN_UOM | NVARCHAR2(6) | Y | Units of measurement / UOM (長さ) |
| 24 | PCM_WGT_UOM | NVARCHAR2(6) | Y | Units of weight / UOM (重量) |
| 25 | PCM_VOL_UOM | NVARCHAR2(6) | Y | Units of volume / UOM (容積) |
| 26 | PCM_LEN | NUMBER(12,6) | Y | Packing Material Length / 梱包資材縦幅 |
| 27 | PCM_WID | NUMBER(12,6) | Y | Packing Material Width / 梱包資材横幅 |
| 28 | PCM_HIG | NUMBER(12,6) | Y | Packing Material Height / 梱包資材高さ |
| 29 | PCM_WGT | NUMBER(12,6) | Y | Packing Material Weight / 梱包資材重量 |
| 30 | PCM_NUM | NUMBER(7) | Y | Numbering / 発番 |
