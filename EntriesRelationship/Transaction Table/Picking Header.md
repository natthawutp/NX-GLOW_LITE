# GWH_TJ_SPK_H
#Entity #Standard #OUTBOUND

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
| 18 | SPKH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | SPKH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | SPKH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | SPKH_PKGP_NUM | NVARCHAR2(16) | N | Picking Group No. |
| 22 | SPKH_BSPK_KND | CHAR(1) | Y | Base Picking Method |
| 23 | SPKH_RLPK_FLG | CHAR(1) | N | Multi Picking Method Flag |
| 24 | SPKH_MTPK_FLG | CHAR(1) | N | Relay Picking Method Flag |
| 25 | SPKH_RDPK_FLG | CHAR(1) | N | Rendezvous Picking Method Flag |
| 26 | SPKH_PKLT_COD | NVARCHAR2(40) | Y | Picking List Report ID |
| 27 | SPKH_ASLT_COD | NVARCHAR2(40) | Y | Assorting List Report ID |
| 28 | SPKH_PK_NUM | NVARCHAR2(16) | Y | Picking Number |
| 29 | SPKH_ASPR_FLG | CHAR(1) | N | Assorting List Printed Flag |
| 30 | SPKH_PKPR_FLG | CHAR(1) | N | Picking List Printed Flag |
| 31 | SPKH_LDPR_FLG | CHAR(1) | N | Loading List Printed Flag |
| 32 | SPKH_SGPR_FLG | CHAR(1) | N | Staging Sheet Printed Flag |
| 33 | SPKH_DLV_COD | NVARCHAR2(80) | Y | Delivery To Code |
| 34 | SPKH_DLV_DEST_REQ | NVARCHAR2(200) | Y | Destination Requirements |
| 35 | SPKH_DLV_CUSS_SLIP_FLG | CHAR(1) | N | Customer-Specific Slip |
| 36 | SPKH_DLV_NOTP_FLG | CHAR(1) | N | Delivery Note Print Flag |
| 37 | SPKH_DLV_LABP_FLG | CHAR(1) | N | Delivery Label Print Flag |
| 38 | SPKH_DLV_STSP_FLG | CHAR(1) | N | Staging Sheet Print Flag |
| 39 | SPKH_DLV_LDLP_FLG | CHAR(1) | N | Loading List Print Flag |
| 40 | SPKH_TOTE_NUM | NVARCHAR2(16) | Y | Tote Number |
| 41 | SPKH_IN_PRG_FLG | CHAR(1) | N |  |
